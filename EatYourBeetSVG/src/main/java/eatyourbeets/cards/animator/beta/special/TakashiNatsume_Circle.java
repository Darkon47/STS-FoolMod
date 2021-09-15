package eatyourbeets.cards.animator.beta.special;

import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import eatyourbeets.cards.animator.beta.colorless.TakashiNatsume;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.TemporaryThousandCutsPower;
import eatyourbeets.powers.common.DeenergizedPower;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorStrings;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class TakashiNatsume_Circle extends AnimatorCard
{
    public enum Form
    {
        None,
        Curse_Delusion,
        Curse_Depression,
        Curse_GriefSeed,
        Curse_Greed,
        Curse_JunTormented,
        Curse_Nutcracker,
        Decay,
        Doubt,
        Necronomicurse,
        Normality,
        Pain,
        Parasite,
        Regret,
        Shame
    }

    private static final AnimatorStrings.Actions ACTIONS = GR.Animator.Strings.Actions;
    private static final int DAMAGE_DECAY = 2;
    private static final int HEAL_NUTCRACKER = 5;
    private static final int NORMALITY_HITS = 3;
    public static final EYBCardData DATA = Register(TakashiNatsume_Circle.class).SetSkill(0, CardRarity.SPECIAL, EYBCardTarget.None).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.NatsumeYuujinchou);
    private TakashiNatsume_Circle.Form currentForm = Form.None;

    public TakashiNatsume_Circle() {
        this(Form.None);
    }

    public TakashiNatsume_Circle(Form form)
    {
        super(DATA);

        Initialize(0, 0, 1, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Dark(1);

        SetPurge(true);
        SetHealing(true);

        ChangeForm(form);
    }

    public void ChangeForm(TakashiNatsume_Circle.Form form) {
        currentForm = form;
        switch (form) {
            case Curse_Delusion:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[3], true);
                break;
            case Curse_Depression:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Draw(secondaryValue, true), true);
                break;
            case Curse_Greed:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.Motivate(secondaryValue, true), true);
                break;
            case Curse_GriefSeed:
            case Decay:
                baseDamage = DAMAGE_DECAY * secondaryValue;
                SetAttackType(EYBAttackType.Elemental);
                SetAttackTarget(EYBCardTarget.ALL);
                this.type = CardType.ATTACK;
                break;
            case Curse_Nutcracker:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.HealHP(HEAL_NUTCRACKER + magicNumber, false), true);
                break;
            case Curse_JunTormented:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true) + " NL  NL " + ACTIONS.ApplyToALL(magicNumber, GR.Tooltips.Frail, true), true);
                break;
            case Doubt:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true), true);
                break;
            case Necronomicurse:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " " + ACTIONS.NextTurnDraw(secondaryValue, true) + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[4], true);
                break;
            case Normality:
                this.cost = 1;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[0],NORMALITY_HITS), true);
                break;
            case Pain:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],secondaryValue), true);
                break;
            case Regret:
                baseDamage = magicNumber;
                SetAttackType(EYBAttackType.Elemental);
                SetAttackTarget(EYBCardTarget.Random);
                this.type = CardType.ATTACK;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            case Shame:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Frail, true), true);
                break;
            default:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.NextTurnDraw(secondaryValue, true), true);
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));

        if (EnergyPanel.totalCount < this.costForTurn) {
            GameActions.Bottom.StackPower(new DeenergizedPower(player, this.costForTurn - EnergyPanel.totalCount));
        }
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        int[] damageMatrix;
        switch(currentForm) {
            case Curse_Delusion:
                GameActions.Bottom.ModifyTag(player.drawPile, 999, AUTOPLAY, false);
                break;
            case Curse_Depression:
                GameActions.Bottom.Draw(secondaryValue);
                break;
            case Curse_JunTormented:
                GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), magicNumber);
                break;
            case Curse_Greed:
                GameActions.Bottom.Motivate(secondaryValue);
                break;
            case Curse_Nutcracker:
                GameActions.Bottom.Heal(HEAL_NUTCRACKER + magicNumber);
                break;
            case Decay:
            case Curse_GriefSeed:
                damageMatrix = DamageInfo.createDamageMatrix(damage, true);
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.FIRE);
                break;
            case Doubt:
                GameActions.Bottom.ApplyWeak(TargetHelper.Enemies(), secondaryValue);
                break;
            case Necronomicurse:
                GameActions.Bottom.DrawNextTurn(secondaryValue);
                if (CombatStats.TryActivateLimited(cardID)) {
                    GameActions.Bottom.SelectFromPile(name,999,player.exhaustPile).SetFilter(card -> card.cardID.equals(TakashiNatsume.DATA.ID)).AddCallback(
                            cards -> {
                                for (AbstractCard card : cards) {
                                    GameActions.Bottom.MoveCard(card,player.drawPile).AddCallback(cCard -> {
                                        GameActions.Bottom.Motivate(card, 1);
                                        AfterLifeMod.Add(card);
                                    });
                                }
                            }
                    );
                }
                break;
            case Normality:
                GameActions.Bottom.StackPower(player, new TakashiNatsumeCirclePower(player, NORMALITY_HITS));
                break;
            case Pain:
                GameActions.Bottom.StackPower(player, new TemporaryThousandCutsPower(player, secondaryValue));
                break;
            case Regret:
                for (int i = 0; i < player.hand.size(); i++) {
                    GameActions.Bottom.DealDamageToRandomEnemy(this, AttackEffects.FIRE);
                }
                break;
            case Shame:
                GameActions.Bottom.ApplyFrail(TargetHelper.Enemies(), secondaryValue);
                break;
            default:
                GameActions.Bottom.DrawNextTurn(secondaryValue);
                break;
        }
    }

    public static class TakashiNatsumeCirclePower extends AnimatorPower
    {
        public TakashiNatsumeCirclePower(AbstractPlayer owner, int amount)
        {
            super(owner, TakashiNatsume_Circle.DATA);

            this.amount = amount;
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount)
        {
            if (this.amount > 0 && info.owner instanceof AbstractMonster && info.type != DamageInfo.DamageType.THORNS && info.type != DamageInfo.DamageType.HP_LOSS)
            {
                GameActions.Top.ApplyPower(this.owner, new StunMonsterPower((AbstractMonster) info.owner, 1));
                this.amount -= 1;
                this.flash();
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void atEndOfRound() {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}