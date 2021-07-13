package eatyourbeets.cards.animator.beta.ultrarare;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.curses.Decay;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.RoomTintEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.subscribers.OnStartOfTurnPostDrawSubscriber;
import eatyourbeets.orbs.animator.Aether;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.ElementalMasteryPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Traveler extends AnimatorCard implements OnStartOfTurnPostDrawSubscriber
{
    public static final EYBCardData DATA = Register(Traveler.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS);
    static
    {
        DATA.AddPreview(new Traveler(Form.Aether, false), true);
        DATA.AddPreview(new Traveler(Form.Lumine, false), true);
    }

    public Traveler.Form currentForm;

    public enum Form
    {
        None,
        Aether,
        Lumine
    }

    public Traveler()
    {
        this(Traveler.Form.None, false);
    }

    private Traveler(Traveler.Form form, boolean upgraded)
    {
        super(DATA);

        Initialize(0, 0, 20, 2);
        SetUpgrade(0, 0, 5, 0);
        SetUnique(true, true);
        SetEthereal(true);
        SetSynergy(Synergies.GenshinImpact);
        this.upgraded = upgraded;
        ChangeForm(form);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeSecondaryValue(1);
        }

        upgradedSecondaryValue = true;
    }


    public void ChangeForm(Traveler.Form form)
    {
        this.currentForm = form;

        switch (form)
        {
            case None:
            {
                LoadImage(null);
                cardText.OverrideDescription(null, true);
                break;
            }

            case Aether:
            {
                LoadImage("_Aether");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[0], true);
                break;
            }

            case Lumine:
            {
                LoadImage("_Lumine");
                this.cardText.OverrideDescription(cardData.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        this.ChangeForm(currentForm == Traveler.Form.Aether ? Traveler.Form.Lumine : Traveler.Form.Aether);
        GameActions.Bottom.MoveCard(this, player.drawPile)
                .ShowEffect(true, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        switch (currentForm)
        {
            case Aether:
                GameActions.Bottom.StackPower(new ElementalMasteryPower(p, magicNumber));
                int evoked = 0;
                for (AbstractOrb orb : player.orbs) {
                    if (!(orb instanceof EmptyOrbSlot) && !Aether.ORB_ID.equals(orb.ID)) {
                        GameActions.Bottom.EvokeOrb(1, orb);
                        GameActions.Bottom.StackPower(new ElementalMasteryPower(p, 5));
                        evoked += 1;
                        if (evoked >= secondaryValue) {
                            break;
                        }
                    }
                }

                GameActions.Bottom.ChannelOrbs(Aether::new, 2);
                break;
            case Lumine:
                for (AbstractMonster target : GameUtilities.GetEnemies(true)) {
                    for (int i = 0; i < secondaryValue; i++) {
                        GameActions.Bottom.ApplyVulnerable(p, target, 1);
                        GameActions.Bottom.ApplyWeak(p, target, 1);
                    }
                }
                Traveler other = (Traveler) makeStatEquivalentCopy();
                other.ChangeForm(this.currentForm);
                CombatStats.onStartOfTurnPostDraw.Subscribe(other);
                GameActions.Bottom.MakeCardInDiscardPile(new Decay());
        }

    }

    @Override
    public void OnStartOfTurnPostDraw()
    {
        int orbCount = CombatStats.OrbsEvokedThisCombat().size();
        if (orbCount > 0)
        {
            GameEffects.Queue.ShowCardBriefly(this);

            AbstractDungeon.effectsQueue.add(new RoomTintEffect(Color.BLACK.cpy(), 0.8F));
            AbstractDungeon.effectsQueue.add(new BorderLongFlashEffect(new Color(1.0F, 1.0F, 1.0F, 0.5F)));

            int startIdx = Math.max(orbCount - magicNumber, 0);

            for (int i = startIdx; i < orbCount; i++)
            {
                AbstractOrb orb = CombatStats.OrbsEvokedThisCombat().get(i);
                orb.onStartOfTurn();
                orb.onEndOfTurn();
            }

            CombatStats.onStartOfTurnPostDraw.Unsubscribe(this);
        }
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        if (currentForm == Traveler.Form.None)
        {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(new Traveler(Form.Aether, upgraded));
            group.group.add(new Traveler(Form.Lumine, upgraded));

            GameActions.Bottom.SelectFromPile(name, 1, group)
                    .SetOptions(false, false)
                    .AddCallback(cards ->
                    {
                        if (cards != null && cards.size() > 0)
                        {
                            Traveler card = (Traveler) cards.get(0);

                            ChangeForm(card.currentForm);
                            GameUtilities.ModifyMagicNumber(card, this.magicNumber, false);
                            GameUtilities.ModifySecondaryValue(card, this.secondaryValue, false);
                        }
                    });
        }
    }
}