package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorPower;
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
        //TODO
        switch (form) {
            case Curse_Nutcracker:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.HealHP(HEAL_NUTCRACKER + magicNumber, true), true);
                break;
            case Curse_JunTormented:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true) + " NL  NL " + ACTIONS.ApplyToALL(magicNumber, GR.Tooltips.Frail, true), true);
                break;
            case Decay:
            case Curse_GriefSeed:
                baseDamage = DAMAGE_DECAY * magicNumber;
                DATA.AttackType = EYBAttackType.Elemental;
                DATA.CardTarget = EYBCardTarget.ALL;
                break;
            case Doubt:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Weak, true), true);
                break;
            case Normality:
                this.cost = 1;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[2],secondaryValue), true);
                break;
            case Pain:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + JUtils.Format(DATA.Strings.EXTENDED_DESCRIPTION[0],secondaryValue), true);
                break;
            case Regret:
                baseDamage = magicNumber;
                DATA.AttackType = EYBAttackType.Elemental;
                DATA.CardTarget = EYBCardTarget.Random;
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + DATA.Strings.EXTENDED_DESCRIPTION[1], true);
                break;
            case Shame:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION + " NL  NL " + ACTIONS.ApplyToALL(secondaryValue, GR.Tooltips.Frail, true), true);
                break;
            default:
                cardText.OverrideDescription(DATA.Strings.DESCRIPTION, true);
        }
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard()
    {
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        int[] damageMatrix;
        switch(currentForm) {
            case Curse_Delusion:
                //GameActions.Bottom.ModifyTag(player.drawPile, 999, AUTOPLAY, false);
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
            case Normality:
                //    int i = 0;
                //    for (AbstractMonster mo : GameUtilities.GetEnemies(true)) {
                //        if (i >= 3) {
                //            GameActions.Bottom.ApplyPower(player, new StunMonsterPower(mo, 1));
                //        }
                //        i++;
                //    }
                break;
            case Pain:
                GameActions.Bottom.StackPower(player, new TakashiNatsumeCirclePower(player, secondaryValue));
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

        public void atEndOfRound() {
            GameActions.Bottom.RemovePower(owner, owner, this);
        }

        public void onAfterCardPlayed(AbstractCard card) {
            int[] damageMatrix = DamageInfo.createDamageMatrix(this.amount, true);
            GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.SLASH_DIAGONAL);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, amount);
        }
    }
}