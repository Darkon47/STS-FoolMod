package eatyourbeets.cards.animator.beta.Bleach;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.interfaces.delegates.ActionT3;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ByakuyaBankai extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ByakuyaBankai.class).SetAttack(-1, CardRarity.SPECIAL, EYBAttackType.Ranged, EYBCardTarget.ALL);

    public ByakuyaBankai()
    {
        super(DATA);

        Initialize(7, 5, 3);
        SetUpgrade(2, 2, 0);

        SetUnique(true, true);
        SetSynergy(Synergies.Bleach);
        SetExhaust(true);
        SetMultiDamage(true);
        SetMartialArtist();
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse > 0)
        {
            effect = energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return effect * amount;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse > 0)
        {
            effect = energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return super.GetDamageInfo().AddMultiplier(effect);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse > 0)
        {
            effect = energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return effect * amount;
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse > 0)
        {
            effect = energyOnUse;
        }

        if (player.hasRelic(ChemicalX.ID))
        {
            effect += ChemicalX.BOOST;
        }

        return super.GetDamageInfo().AddMultiplier(effect);
    }


    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        GameActions.Bottom.MoveCard(this, player.exhaustPile, player.discardPile)
                .ShowEffect(false, false);
        if (this.canUpgrade())
        {
            this.upgrade();
            this.flash();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (timesUpgraded < magicNumber)
        {
            GameActions.Bottom.Callback(card -> {
                ChooseAction(m);
            });
        }
    }

    private void ChooseAction(AbstractMonster m)
    {
        AnimatorCard damage = BuildCard(GenerateInternal(CardType.ATTACK, this::DamageEffect), m);
        AnimatorCard block = BuildCard(GenerateInternal(CardType.SKILL, this::BlockEffect), m);

        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        choices.addToTop(damage);
        choices.addToBottom(block);

        Execute(choices, m);
    }

    private AnimatorCardBuilder GenerateInternal(AbstractCard.CardType type, ActionT3<AnimatorCard, AbstractPlayer, AbstractMonster> onUseAction)
    {
        AnimatorCardBuilder builder = new AnimatorCardBuilder(ByakuyaBankai.DATA.ID);
        builder.SetText(name, "", "");
        builder.SetProperties(type, GR.Enums.Cards.THE_ANIMATOR, AbstractCard.CardRarity.RARE, CardTarget.ENEMY);
        builder.SetOnUse(onUseAction);

        if (type.equals(CardType.ATTACK))
        {
            builder.SetAttackType(EYBAttackType.Ranged, EYBCardTarget.ALL, GameUtilities.UseXCostEnergy(this));
            builder.SetNumbers(damage, 0, 0, 0);
        }
        else
        {
            builder.attributeMultiplier = GameUtilities.UseXCostEnergy(this);
            builder.SetNumbers(0, block, 0, 0);
        }

        return builder;
    }

    private AnimatorCard BuildCard(AnimatorCardBuilder builder, AbstractMonster m)
    {
        AnimatorCard card = builder.Build();
        card.applyPowers();
        card.calculateCardDamage(m);
        return card;
    }

    private void Execute(CardGroup group, AbstractMonster m)
    {
        GameActions.Top.SelectFromPile(name, 1, group)
                .SetOptions(false, false)
                .AddCallback(cards ->
                {
                    AbstractCard card = cards.get(0);
                    card.applyPowers();
                    card.calculateCardDamage(m);
                    card.use(player, m);
                });
    }

    private void DamageEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.VFX(new BorderLongFlashEffect(Color.WHITE));
        GameActions.Bottom.VFX(new ShockWaveEffect(p.hb.cX, p.hb.cY, Color.WHITE, ShockWaveEffect.ShockWaveType.ADDITIVE), 0.75f);

        for (int i=0; i<GameUtilities.UseXCostEnergy(this); i++)
        {
            GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    private void BlockEffect(AbstractCard card, AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<GameUtilities.UseXCostEnergy(this); i++)
        {
            GameActions.Bottom.GainBlock(block);
        }
    }
}